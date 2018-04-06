(ns erc20-mapper.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [erc20-mapper.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [erc20-mapper.env :refer [defaults]]
            [mount.core :as mount]
            [manifold.deferred :as d]
            [erc20-mapper.middleware :as middleware]))

(defn ring->aleph [handler]
  (fn [request]
    (let [response (d/deferred)]
      (handler request #(d/success! response %) #(d/error! response %))
      response)))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  (ring->aleph
   (middleware/wrap-base
    (routes
     #'service-routes
     (route/not-found "page not found")))))
