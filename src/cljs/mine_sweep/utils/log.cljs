(ns mine-sweep.utils.log
  (:require [mine-sweep.config :refer [debug?]]))

(defn log [& args]
  (when debug?
    (js/console.log args)))
