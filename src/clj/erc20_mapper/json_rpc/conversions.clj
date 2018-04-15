(ns erc20-mapper.json-rpc.conversions)

(defprotocol Hexable
  (int->hex [_]))

(defn without-0x [s]
  (clojure.string/replace s #"^0x" ""))

(defn hex->int [s]
  (BigInteger. (without-0x s) 16))

(extend-type java.lang.Long
  Hexable
  (int->hex [i] (int->hex (biginteger i))))

(extend-type clojure.lang.BigInt
  Hexable
  (int->hex [i] (int->hex (biginteger i))))

(extend-type java.math.BigInteger
  Hexable
  (int->hex [i] (str "0x" (format "%060x" i))))
