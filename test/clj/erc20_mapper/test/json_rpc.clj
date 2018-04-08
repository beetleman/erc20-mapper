(ns erc20-mapper.test.json-rpc
  (:require [clojure.test :refer :all]
            [erc20-mapper.json-rpc :as json-rpc]))

(deftest test-without-0x
  (testing "if remove 0x if exist"
    (is (= (json-rpc/without-0x "0x1fa")
           "1fa")))
  (testing "pass string without 0x"
    (is (= (json-rpc/without-0x "1fa")
           "1fa"))))

(deftest test-hex->int
  (testing "converting hex string to integer"
    (is (= (json-rpc/hex->int "0x11")
           17))))

(deftest test-int->hex
  (testing "converting integer to hex string"
    (is (= (json-rpc/int->hex 15)
           "0xf"))))

(deftest test-request
  (let [method "fakeMethod"
        params ["fakeParam_1" "fakeParam_2"]]
    (testing "method without params"
      (is (= (json-rpc/request method)
             {"jsonrpc" "2.0"
              "method"   method
              "params"   [],
              "id"       1})))
    (testing "method with params"
      (is (= (json-rpc/request method params)
              {"jsonrpc" "2.0"
              "method"   method
              "params"   params,
              "id"       1})))))

(deftest test-blockNumber
  (let [{:keys [handler request]} (#'json-rpc/blockNumberDesc)]
    (testing "request"
      (is (= request
             {"jsonrpc" "2.0"
              "method"  "eth_blockNumber"
              "params"  []
              "id"      1})))
    (testing "handler"
      (is (= (handler "0xa")
             10)))))

(deftest test-getBlockByNumber
  (let [{:keys [handler request]} (#'json-rpc/getBlockByNumberDesc 1)]
    (testing "request"
      (is (= request
             {"jsonrpc" "2.0"
              "method"  "eth_getBlockByNumber"
              "params"  ["0x1" true]
              "id"      1})))
    (testing "handler"
      (is (= (handler :fake-body)
             :fake-body)))))

(deftest test-getTransactionReceipt
  (let [{:keys [handler request]} (#'json-rpc/getTransactionReceiptDesc "0xf")]
    (testing "request"
      (is (= request
             {"jsonrpc" "2.0"
              "method"  "eth_getTransactionReceipt"
              "params"  ["0xf"]
              "id"      1})))
    (testing "handler"
      (is (= (handler :fake-body)
             :fake-body)))))
