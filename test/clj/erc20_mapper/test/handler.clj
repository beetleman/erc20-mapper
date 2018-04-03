(ns erc20-mapper.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [erc20-mapper.handler :refer :all]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'erc20-mapper.config/env
                 #'erc20-mapper.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))
