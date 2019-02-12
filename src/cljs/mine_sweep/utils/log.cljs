(ns mine-sweep.utils.log)

(defn log [& args]
  (when goog.DEBUG
    (apply println args)))
