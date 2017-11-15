(ns user
  (:require [mount.core :as mount]
            [exercize.figwheel :refer [start-fw stop-fw cljs]]
            exercize.core))

(defn start []
  (mount/start-without #'exercize.core/http-server
                       #'exercize.core/repl-server))

(defn stop []
  (mount/stop-except #'exercize.core/http-server
                     #'exercize.core/repl-server))

(defn restart []
  (stop)
  (start))


