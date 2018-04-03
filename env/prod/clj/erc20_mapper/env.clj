(ns erc20-mapper.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[erc20-mapper started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[erc20-mapper has shut down successfully]=-"))
   :middleware identity})
