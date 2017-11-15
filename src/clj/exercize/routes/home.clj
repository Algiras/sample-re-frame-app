(ns exercize.routes.home
  (:require [exercize.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/data" []
       (-> (response/ok (-> "data/data.json" io/resource slurp))
           (response/header "Content-Type" "application/json; charset=utf-8"))))

