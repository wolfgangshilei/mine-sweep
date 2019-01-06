(ns mine-sweep.ui.common.constants)

(def cell-size 17)

(defn- init-cells-pos
  [n-row n-col]
  (for [x (range n-row)
        y (range n-col)]
    [x y]))

(defn- init-neighbours-for-cell
  [[x y] n-row n-col]
  (for [row ((juxt inc dec identity) x)
        col ((juxt inc dec identity) y)
        :when (and (>= row 0)
                   (>= col 0)
                   (< row n-row)
                   (< col n-col)
                   (or (not= row x)
                       (not= col y)))]
    [row col]))

(def levels {:easy   {:n-mine         10
                      :n-row          9
                      :n-col          9
                      :cells-pos-data (->> (map #(vector % (init-neighbours-for-cell % 9 9))
                                           (init-cells-pos 9 9))
                                           (into (sorted-map)))}
             :medium {:n-mine         40
                      :n-row          16
                      :n-col          16
                      :cells-pos-data (->> (map #(vector % (init-neighbours-for-cell % 16 16))
                                                (init-cells-pos 16 16))
                                           (into (sorted-map)))}
             :hard   {:n-mine         99
                      :n-row          16
                      :n-col          30
                      :cells-pos-data (->> (map #(vector % (init-neighbours-for-cell % 16 30))
                                           (init-cells-pos 16 30))
                                           (into (sorted-map)))}})

(defn config-for-level
  [level]
  (get levels level))
