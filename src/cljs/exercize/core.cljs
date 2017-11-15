(ns exercize.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [exercize.ajax :refer [load-interceptors!]]
            [exercize.handlers]
            [exercize.subscriptions]
            [exercize.views :as v]
            [re-frisk.core :refer [enable-re-frisk!]])
  (:import goog.History))


(def pages
  {:home #'v/home-page
   :user #'v/user-page})

(defn page []
  [:div
   [(pages @(rf/subscribe [:page]))]])
 
;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/:id" [id]
  (rf/dispatch [:set-active-page :user (js/parseInt id)]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (enable-re-frisk!)
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
