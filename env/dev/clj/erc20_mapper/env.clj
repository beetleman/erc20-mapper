(ns erc20-mapper.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [erc20-mapper.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[erc20-mapper started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[erc20-mapper has shut down successfully]=-"))
   :middleware wrap-dev})
