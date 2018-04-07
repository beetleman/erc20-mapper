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


;; Protocols & Records

(defprotocol SendAsync
  (-send [_]))

(defrecord MethodDesc [request handler]
  SendAsync
  (-send [_]
    (d/chain
      (send-request request)
      handler)))

(defn- blockNumberDesc []
  (->MethodDesc (request "eth_blockNumber")
                #(-> %
                 :body
                 :result
                 hex->int)))

(defn blockNumber []
  (-send (blockNumberDesc)))
