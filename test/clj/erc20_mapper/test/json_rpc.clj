(ns erc20-mapper.test.json-rpc
  (:require [clojure.test :refer :all]
            [erc20-mapper.json-rpc :as json-rpc]))

(defn create-response [body]
  {:request-time    645,
   :headers
   {"content-type" "application/json"}
   :status          200
   :keep-alive?     true
   :connection-time 479
   :body            body})


(deftest test-without-0x
  (testing "if remove 0x if exist"
    (is (= (json-rpc/without-0x "0x1fa")
           "1fa")))
  (testing "pass string without 0x"
    (is (= (json-rpc/without-0x "1fa")
           "1fa"))))

(deftest test-hex->int
  (testing "converting string to number"
    (is (= (json-rpc/hex->int "0x11")
           17))))

(deftest test-blockNumber
  (let [{:keys [handler request]} (#'json-rpc/blockNumberDesc)]
    (testing "request"
      (is (= request
             {"jsonrpc" "2.0", "method" "eth_blockNumber", "params" [], "id" 1})))
    (testing "handler"
      (is (= (handler (create-response {:jsonrpc "2.0", :id 1, :result "0xa"}))
             10)))))
