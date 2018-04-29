(ns erc20-mapper.test.entities
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [erc20-mapper.entities :as entities]))

(def raw-transferEvent
  {:address             "0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570",
   :transactionHash     "0x8e0da0065244bf56e51c0902f8c5351988d9afc3bb29ea4e8de3cb94761b6a20",
   :blockHash           "0xff9b8efc0473050843605578c81aaa9903eb9b2685f35938fbe0432cc78a5a08",
   :transactionLogIndex "0x6",
   :type                "mined",
   :transactionIndex    "0x1",
   :topics              ["0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"
                         "0x0000000000000000000000007447dab10325f902725191a34eb8288abe02c7f4"
                         "0x000000000000000000000000173a2467cece1f752eb8416e337d0f0b58cad795"],
   :blockNumber         "0x6be7cb",
   :logIndex            "0x6",
   :data                "0x00000000000000000000000000000000000000000000000000b8744cff89cbaa"})

(def transferEvent
  {:address             "0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570",
   :transactionHash     "0x8e0da0065244bf56e51c0902f8c5351988d9afc3bb29ea4e8de3cb94761b6a20",
   :blockHash           "0xff9b8efc0473050843605578c81aaa9903eb9b2685f35938fbe0432cc78a5a08",
   :transactionLogIndex 6,
   :type                "mined",
   :transactionIndex    1,
   :topics              ["0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"
                         "0x0000000000000000000000007447dab10325f902725191a34eb8288abe02c7f4"
                         "0x000000000000000000000000173a2467cece1f752eb8416e337d0f0b58cad795"],
   :blockNumber         7071691,
   :logIndex            6,
   :data                "0x00000000000000000000000000000000000000000000000000b8744cff89cbaa"})


(deftest test-raw-transferEvent
  (testing "check explain for corect data"
    (is (= nil
           (s/explain-data ::entities/raw-transferEvent raw-transferEvent)))))

(deftest test-transferEvent
  (testing "check explain for corect data"
    (is (= nil
           (s/explain-data ::entities/transferEvent transferEvent)))))
