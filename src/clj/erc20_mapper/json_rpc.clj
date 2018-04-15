(ns erc20-mapper.json-rpc
  (:require [aleph.http :as http]
            [cheshire.core :as json]
            [erc20-mapper.config :refer [jsonrpc-url]]
            [erc20-mapper.json-rpc.conversions :as conversions]
            [manifold.deferred :as d]))

;; Utils

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
                conversions/hex->int))

(defn blockNumber []
  (-send (blockNumberDesc)))

(defn- getBlockByNumberDesc [blockNumber]
  (->MethodDesc (request "eth_getBlockByNumber" [(conversions/int->hex blockNumber) true])
                identity))

(defn getBlockByNumber [blockNumber]
  (-send (getBlockByNumberDesc blockNumber)))


(defn getTransactionReceiptDesc [txHash]
  (->MethodDesc (request "eth_getTransactionReceipt" [txHash])
                identity))

(defn getTransactionReceipt [txHash]
  (-send (getTransactionReceiptDesc txHash)))
