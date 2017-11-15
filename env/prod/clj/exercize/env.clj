(ns exercize.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[exercize started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[exercize has shut down successfully]=-"))
   :middleware identity})
