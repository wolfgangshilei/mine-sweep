(ns mine-sweep.ui.common.constants)

(def cell-size 17)
#_(def cell-gap  "0")
(def levels {:easy   {:n-mine 10
                      :n-row  9
                      :n-col  9}
             :medium {:n-mine 40
                      :n-row  16
                      :n-col  16}
             :hard   {:n-mine 99
                      :n-row  16
                      :n-col  30}})

(defn config-for-level
  [level]
  (get levels level))
