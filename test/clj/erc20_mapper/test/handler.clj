(ns erc20-mapper.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [erc20-mapper.handler :refer :all]
            [cheshire.core :as json]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'erc20-mapper.config/env
                 #'erc20-mapper.handler/app)
    (f)))

(defn read-body
  "read and decode body of response"
  [response]
  (json/decode (slurp (:body response)) true))

(deftest test-app
  (testing "healthy"
    (let [response (app (request :get "/api"))]
      (is (= 200 (:status @response)))
      (is (= {:healthy true :version "0.1.0-SNAPSHOT"}
             (read-body @response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status @response))))))
