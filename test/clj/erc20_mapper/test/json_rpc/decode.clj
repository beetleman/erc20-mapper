(ns erc20-mapper.test.json-rpc.decode
  (:require [clojure.test :refer :all]
            [erc20-mapper.json-rpc.decode :as decode]))

;; https://github.com/pelle/cloth/blob/838189da152f4112065a8a9db0b4b7663947b97c/src/cloth/util.cljc#L489
;; mainnet transaction: 0xf0f8156f8843e89d2990751b131a4f9209d987e34b73cd7dc13b8752d9165b37
;; EOS Token
(def log-data "0x000000000000000000000000000000000000000000000000f2565fdb2510a000")
(def indexed-topic-1 "0x000000000000000000000000f3e36ad56aa85abdacc18c02d19509ae4f7d5899")
(def indexed-topic-2 "0x00000000000000000000000009390040090e625ad2592f99bd58f1521896543c")
(def log {:address          "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0"
          :transactionHash  "0xf0f8156f8843e89d2990751b131a4f9209d987e34b73cd7dc13b8752d9165b37"
          :blockHash        "0x04f4d0df1d27c4606c88e78800e8b288b9099cc1afaae1082dff82304770d468"
          :transactionIndex 137
          :topics           ["0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"
                             indexed-topic-1
                             indexed-topic-2]
          :blockNumber      5520487
          :logIndex         131
          :removed          false
          :data             log-data})

(deftest test-eth-data-chunk
  (testing "uint256"
    (is (= {:decoded 17462250000000000000
            :data    ""}
           (decode/eth-data-chunk :uint256
                                  log-data))))
  (testing "address"
    (is (= {:decoded "0xf3e36ad56aa85abdacc18c02d19509ae4f7d5899"
            :data    ""}
           (decode/eth-data-chunk :address
                                  indexed-topic-1)))))

(deftest test-event-log
  (testing "EOS event `Transfer(address indexed from, address indexed to, uint256 value)`"
    (is (= ["0xf3e36ad56aa85abdacc18c02d19509ae4f7d5899"
            "0x09390040090e625ad2592f99bd58f1521896543c"
            17462250000000000000]
           (decode/event-log [{:type    :address
                               :indexed true}
                              {:type    :address
                               :indexed true}
                              {:type    :uint256
                               :indexed false}]
                             log)))))
