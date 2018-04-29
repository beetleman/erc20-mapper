(ns erc20-mapper.entities
  (:require [clojure.spec.alpha :as s]
            [com.gfredericks.test.chuck.generators :refer [string-from-regex]]
            [erc20-mapper.consts :as consts]
            [clojure.pprint :refer [cl-format]]
            [clojure.spec.gen.alpha :as gen]
            [spec-tools.spec :as spec]))

;; UTILS:

(defn- create-hex-regex
  ([min-length max-length]
   (create-hex-regex min-length max-length false))
  ([min-length max-length zero-x?]
   (let [prefix (if zero-x?
                  "0x0*"
                  "")]
     (re-pattern (str prefix "[0-9a-f]{" min-length  "," max-length "}")))))

(defn- hex-regex-based-spec
  ([length]
   (hex-regex-based-spec length length))
  ([min-length max-length]
   (hex-regex-based-spec min-length max-length max-length))
  ([min-length max-length pad-to]
   (let [hex-regex    (create-hex-regex min-length max-length)
         hex-regex-0x (create-hex-regex min-length max-length true)]
     (s/with-gen (s/and spec/string?
                        #(<= min-length
                             (count %)
                             (+ (if (< pad-to max-length)
                                 max-length
                                 pad-to)
                                2))
                        #(re-matches hex-regex-0x %))
       #(gen/fmap (fn [s]
                    (cl-format nil (str "0x~" pad-to ",'" \0 "d") s))
                  (string-from-regex hex-regex))))))

;; SPECS:

(s/def ::blockNumber spec/pos-int?)
(s/def ::pos-int-0 spec/nat-int?)
(s/def ::logIndex ::pos-int-0)
(s/def ::transactionIndex ::pos-int-0)
(s/def ::transactionLogIndex ::pos-int-0)


(s/def ::address (hex-regex-based-spec 1 consts/length-of-address))

(s/def ::hex-256bit (hex-regex-based-spec 1 consts/length-of-256-bit-hex))
(s/def ::hex-256bit-without-pad (hex-regex-based-spec 1 consts/length-of-256-bit-hex 1))

(s/def ::topic-uint256 ::hex-256bit)

(s/def ::blockHash ::hex-256bit)

(s/def ::transactionHash ::hex-256bit)

(s/def ::eventSignature ::hex-256bit)

;TODO: spot rest of types
(s/def ::type #{"mined"})

(s/def ::topic-address (hex-regex-based-spec 1
                                             consts/length-of-address
                                             consts/length-of-256-bit-hex))

(s/def :transferEvent/data ::topic-uint256)

(s/def :transferEvent/topics (s/cat :signature #{consts/transferEventSignature}
                                    :from ::topic-address
                                    :to ::topic-address))

(s/def ::transferEvent (s/keys :req-un [::address
                                        ::transactionHash
                                        ::blockHash
                                        :transferEvent/topics
                                        ::blockNumber
                                        ::logIndex
                                        ::transactionIndex
                                        ::transactionLogIndex
                                        ::type
                                        :transferEvent/data]))

(s/def :raw/transactionIndex ::hex-256bit-without-pad)
(s/def :raw/transactionLogIndex ::hex-256bit-without-pad)
(s/def :raw/logIndex ::hex-256bit-without-pad)
(s/def :raw/blockNumber ::hex-256bit-without-pad)

(s/def ::raw-transferEvent (s/keys :req-un [::address
                                            ::transactionHash
                                            ::blockHash
                                            :transferEvent/topics
                                            :raw/blockNumber
                                            :raw/logIndex
                                            :raw/transactionIndex
                                            :raw/transactionLogIndex
                                            ::type
                                            :transferEvent/data]))
