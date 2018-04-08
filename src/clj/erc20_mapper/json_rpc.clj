(ns erc20-mapper.json-rpc
  (:require [aleph.http :as http]
            [cheshire.core :as json]
            [manifold.deferred :as d]
            [clojure.tools.logging :as log]
            [erc20-mapper.config :refer [jsonrpc-url]]))

;; Utils

(defn without-0x [s]
  (clojure.string/replace s #"^0x" ""))

(defn hex->int [s]
  (Integer/parseInt (without-0x s)
                    16))

(defn int->hex [i]
  (str "0x" (Integer/toHexString i)))

(defn send-request
  [request]
  (http/post jsonrpc-url
             {:body         (json/encode request)
              :accept       :json
              :content-type :json
              :as           :json}))

(defn request
  ([method]
   (request method []))
  ([method params]
   {"jsonrpc" "2.0"
    "method"  method
    "params"  params
    "id"      1}))

(defn get-result [response]
  (get-in response [:body :result]))

;; Protocols & Records

(defprotocol SendAsync
  (-send [_]))

(defrecord MethodDesc [request handler]
  SendAsync
  (-send [_]
    (d/chain
     (send-request request)
     get-result
     handler)))

(defn- blockNumberDesc []
  (->MethodDesc (request "eth_blockNumber")
                hex->int))

(defn blockNumber []
  (-send (blockNumberDesc)))

(defn- getBlockByNumberDesc [blockNumber]
  (->MethodDesc (request "eth_getBlockByNumber" [(int->hex blockNumber) true])
                identity))

(defn getBlockByNumber [blockNumber]
  (-send (getBlockByNumberDesc blockNumber)))


(defn getTransactionReceiptDesc [txHash]
  (->MethodDesc (request "eth_getTransactionReceipt" [txHash])
                identity))

(defn getTransactionReceipt [txHash]
  (-send (getTransactionReceiptDesc txHash)))
