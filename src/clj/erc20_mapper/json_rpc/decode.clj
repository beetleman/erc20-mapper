(ns erc20-mapper.json-rpc.decode
  (:require [erc20-mapper.json-rpc.conversions :as conversions]))

(def address {:memory  [20 :bytes]
              :decoder identity})
(def uint256 {:memory  [256 :bits]
              :decoder conversions/hex->int})
(def types {:address address
            :uint256 uint256})

(defn keyword->type [k]
  (get types k))

(defn decode [type data]
  ((:decoder type) data))

(defmulti size-of (comp last :memory))
(defmethod size-of :bits [{[value] :memory}]
  (/ value 4))
(defmethod size-of :bytes [{[value] :memory}]
  (* value 2))

(defn get-data-chunk [type data]
  (let [size      (size-of type)
        remainder (mod size 64)
        padding   (if (zero? remainder)
                    0
                    (- 64 remainder))
        split-at  (+ size padding)]
    [(subs (subs data 0 split-at) padding)
     (subs data split-at)]))

(defn eth-data [[type-kw & types-kw] data]
  (when type-kw
    (let [data         (conversions/without-0x data)
          type         (keyword->type type-kw)
          [chunk data] (get-data-chunk type data)
          decoded      (decode type chunk)]
      (lazy-seq (cons decoded (eth-data types-kw data))))))
