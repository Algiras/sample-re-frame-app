(ns exercize.handlers
  (:require [exercize.db :as db]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx debug after]]
            [clojure.string :refer [lower-case]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [json-response-format]]
            [clojure.spec :as s]))

(defn check-and-throw
  "throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :exercize.db/db)))

(reg-event-db
 :users-load-success
 check-spec-interceptor
 (fn [db [_ users]]
   (assoc db :users users :loading false)))

(reg-event-db
 :users-load-failure
 check-spec-interceptor
 (fn [db [_ users]]
   (assoc db :users users :loading false)))

(reg-event-fx
  :initialize-db
  (fn [_ _]
    {:db   (assoc db/default-db :loading true)
     :http-xhrio {:method          :get
                  :uri             "/data"
                  :timeout         8000
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:users-load-success]
                  :on-failure      [:users-load-failure]}}))

(reg-event-db
 :set-filter
 check-spec-interceptor
 (fn [db [_ filter-value]]
   (assoc db :filter filter-value)))

(reg-event-db
 :set-order-by
 check-spec-interceptor
 (fn [db [_ type method]]
   (assoc db :order-by {:type type :method method})))

(reg-event-db
 :set-filter-type
 check-spec-interceptor
 (fn [db [_ filter-type]]
   (assoc db :filter-type filter-type)))

(reg-event-db
 :set-active-page
 check-spec-interceptor

 (fn [db [_ page id]]
   (assoc db :page page :user-id id)))

(reg-event-db
 :set-user
 check-spec-interceptor
 (fn [db [_ user]]
   (assoc db :user-id user)))
