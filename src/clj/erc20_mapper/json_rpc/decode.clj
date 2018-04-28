(ns erc20-mapper.json-rpc.decode
  (:require [erc20-mapper.json-rpc.conversions :as conversions]))

(def address {:memory  [20 :bytes]
              :decoder conversions/with-0x})
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

(defn eth-data-chunk [type-kw data]
  (let [data         (conversions/without-0x data)
        type         (keyword->type type-kw)
        [chunk data] (get-data-chunk type data)
        decoded      (decode type chunk)]
    {:data    data
     :decoded decoded}))

(defn- -event-data [[{type-kw :type indexed :indexed} & abi] data indexed-data]
  (lazy-seq
   (cond
     (nil? type-kw)
     nil

     indexed
     (cons (:decoded (eth-data-chunk type-kw (first indexed-data)))
           (-event-data abi data (rest indexed-data)))

     :default
     (let [{:keys [decoded data]} (eth-data-chunk type-kw data)]
       (cons decoded
             (-event-data abi data indexed-data))))))
(defn event-log [abi log]
  (let [{[_ & indexed-data] :topics
         data :data} log]
    (-event-data abi data indexed-data)))
