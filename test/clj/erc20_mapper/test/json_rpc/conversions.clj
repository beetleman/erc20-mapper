(ns erc20-mapper.test.json-rpc.conversions
  (:require [clojure.test :refer :all]
            [erc20-mapper.json-rpc.conversions :as conversions]))

(deftest test-without-0x
  (testing "if remove 0x if exist"
    (is (= (conversions/without-0x "0x1fa")
           "1fa")))
  (testing "pass string without 0x"
    (is (= (conversions/without-0x "1fa")
           "1fa"))))

(deftest test-with-0x
  (testing "pass string with 0x"
    (is (= (conversions/with-0x "0x1fa")
           "0x1fa")))
  (testing "add 0x if string without 0x"
    (is (= (conversions/with-0x "1fa")
           "0x1fa"))))

(deftest test-hex->int
  (testing "converting hex string to integer"
    (is (= (conversions/hex->int "0x000000000000000000000000000000000000000000000000000000000011")
           17))))

(deftest test-int->hex
  (testing "converting integer to hex string"
    (is (= (conversions/int->hex 15)
           "0x00000000000000000000000000000000000000000000000000000000000f"))))
