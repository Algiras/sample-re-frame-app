(ns exercize.subscriptions
  (:require [re-frame.core :refer [reg-sub]]
            [clojure.string :refer [starts-with? lower-case]]))

(reg-sub
 :filter-type
 (fn [db _]
   (:filter-type db)))

(reg-sub
 :filter-types
 (fn [db _]
   (:filter-types db)))

(reg-sub
 :is-loading?
 (fn [db _]
   (:loading db)))

(reg-sub
 :filter-type-name
 :<- [:filter-type]
 :<- [:filter-types]
 (fn [[filter-type filter-types] _]
   (:name (first (filter #(= filter-type (:type %)) filter-types)))))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
 :users
 (fn [db _]
   (:users db)))

(reg-sub
 :filter
 (fn [db _]
   (:filter db)))

(reg-sub
 :order-by
 (fn [db _]
   (:order-by db)))

(reg-sub
 :order-by-types
 (fn [db _]
   (:order-by-types db)))

(reg-sub
 :order-types
 (fn [db _]
   (:order-types db)))

(reg-sub
 :filtered-users
 :<- [:filter]
 :<- [:filter-type]
 :<- [:order-by]
 :<- [:users]
 (fn [[filter-value filter-type order-by users] _]
   (let [order-func #(sort-by (:type order-by) (if (= :asc (:method order-by)) < >) %)]
     (if (empty? filter-value)
       (order-func users)
       (order-func (filter #(starts-with? (filter-type %) filter-value) users))))))

(reg-sub
 :user-id
 (fn [db _]
   (:user-id db)))

(reg-sub
 :user-info
 :<- [:users]
 :<- [:user-id]
 (fn [[users user-id] _]
   (if (> user-id 0)
     (first (filter #(= user-id (:id %)) users)))))
