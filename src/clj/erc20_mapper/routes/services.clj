(ns erc20-mapper.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [spec-tools.spec :as spec]
            [clojure.spec.alpha :as s]))

(s/def ::x spec/int?)
(s/def ::y spec/int?)
(s/def ::total spec/int?)
(s/def ::total-map (s/keys :req-un [::total]))

(s/def ::healthy spec/boolean?)
(s/def ::version spec/string?)
(s/def ::status (s/keys :req-un [::healthy ::version]))

(def version (System/getProperty "erc20-mapper.version"))

(def service-routes
  (api
   {:swagger {:ui "/"
              :spec "/swagger.json"
              :data {:info {:version version
                            :title "Sample API"
                            :description "Sample Services"}}}}
   (context "/api" []
    :tags ["ERC-20"]
    :coercion :spec

    (GET "/" []
      :summary "status"
      :return ::status
      (ok {:healthy true
           :version version}))

    (POST "/plus" []
      :summary "plus"
      :body-params [x :- ::x, {y :- ::y 0}]
      :return ::total-map
      (ok {:total (+ x y)})))))
