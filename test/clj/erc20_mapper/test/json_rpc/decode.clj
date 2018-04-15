(ns erc20-mapper.test.json-rpc.decode
  (:require [clojure.test :refer :all]
            [erc20-mapper.json-rpc.decode :as decode]))

;; https://github.com/pelle/cloth/blob/838189da152f4112065a8a9db0b4b7663947b97c/src/cloth/util.cljc#L489
(def log {:address             "0xc2ce67affc8bf3e6bdb87be40dc104addb5f66a4"
          :transactionHash     "0x08c9bef894e1e5a4ea129043954f69b9ffd8ccf0a155ea43b861fb7bc7c69683"
          :blockHash           "0x9c512510eedf6699f87293577a74409b0209ebc6be1da3cb75d519d33949c8a1"
          :transactionLogIndex "0x0"
          :type                "mined"
          :transactionIndex    0
          :topics              [;; hash of parametrs of event
                                "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"
                                ;; data of first indexed parametr, decode by re

                                ;; data of second indexed parapetr
                                "0x000000000000000000000000ef59cb8748e54ea2a7aaa0699430271000000000"]
          :blockNumber 139530
          :logIndex    0
          ;; data of all not indexed parameters, paded to 64
          :data        "0x00000000000000000000000000000000000000000000000000000000000002f2"})

(deftest test-eth-data
  (testing "uint256"
    (is (= [754]
           (decode/eth-data [:uint256]
                            "0x00000000000000000000000000000000000000000000000000000000000002f2"))))
  (testing "address"
    (is (= ["00a329c0648769a73afac7f9381e08fb43dbea72"]
           (decode/eth-data [:address]
                            "0x00000000000000000000000000a329c0648769a73afac7f9381e08fb43dbea72")))))
