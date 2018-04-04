(ns erc20-mapper.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [spec-tools.spec :as spec]
            [clojure.spec.alpha :as s]))

(s/def ::x spec/int?)
(s/def ::y spec/int?)
(s/def ::total spec/int?)
(s/def ::total-map (s/keys :req-un [::total]))

(def service-routes
  (api
   {:swagger {:ui "/swagger-ui"
              :spec "/swagger.json"
              :data {:info {:version "1.0.0"
                            :title "Sample API"
                            :description "Sample Services"}}}}
   (context "/api" []
    :tags ["example"]
    :coercion :spec

    (POST "/plus" []
      :summary "plus"
      :body-params [x :- ::x, {y :- ::y 0}]
      :return ::total-map
      (ok {:total (+ x y)})))))
