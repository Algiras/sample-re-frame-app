(ns exercize.db
  (:require [cljs.spec :as s]))

(def pageOptions #{:home :user})
(def genders #{"Female" "Male"})
(def filterTypes #{:first_name :last_name :email})
(def propertyTypes #{{:name "First Name" :type :first_name}
                     {:name "Last Name" :type :last_name}
                     {:name "Email" :type :email}})

(def orderMethods #{:asc :desc})
(def orderTypes #{{:name "Descending" :method :desc}
                  {:name "Asceding" :method :asc}})

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(s/def ::id int?)
(s/def ::first_name string?)
(s/def ::last_name string?)
(s/def ::email ::email-type)
(s/def ::gender (s/and string? genders))

(s/def ::picture string?)

(s/def ::friend (s/keys :req-un [::first_name ::last_name ::email ::picture]))
(s/def ::friends (s/coll-of ::friend))
(s/def ::user (s/keys :req-un [::id ::first_name ::last_name ::email ::gender ::friends]))
(s/def ::users (s/coll-of ::user))

(s/def ::user-id (s/or :id ::id
                       :empty nil?))

(s/def ::name string?)
(s/def ::type keyword?)
(s/def ::type-option (s/and
                      (s/keys :req-un [::name ::type])
                      propertyTypes))

(s/def ::method (s/and
                 orderMethods
                 keyword?))
(s/def ::order-type (s/and
                     (s/keys :req-un [::name ::method])
                     orderTypes))
(s/def ::order-by (s/keys :req-un [::type ::method]))

(s/def ::page (s/and
               keyword?
               pageOptions))
(s/def ::filter string?)

(s/def ::filter-type (s/and
                      keyword?
                      filterTypes))

(s/def ::type-options (s/and
                       (s/coll-of ::type-option)))

(s/def ::filter-types ::type-options)
(s/def ::order-by-types ::type-options)
(s/def ::order-types (s/coll-of ::order-type))

(s/def ::loading boolean?)

(s/def ::db (s/keys :req-un [::page
                             ::filter
                             ::filter-type
                             ::filter-types
                             ::order-by
                             ::order-by-types
                             ::order-types
                             ::users
                             ::user-id
                             ::loading]))


(def default-db
  {:page :home
   :filter ""
   :filter-type :last_name
   :filter-types [{:name "First Name" :type :first_name}
                    {:name "Last Name" :type :last_name}
                    {:name "Email" :type :email}]
   :order-by {:type :first_name :method :desc}
   :order-by-types [{:name "First Name" :type :first_name}
                    {:name "Last Name" :type :last_name}
                    {:name "Email" :type :email}]
   :order-types [{:name "Descending" :method :desc}
                 {:name "Asceding" :method :asc}]
   :users []
   :user-id nil
   :loading false})




