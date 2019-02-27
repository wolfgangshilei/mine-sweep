(ns mine-sweep.ui.record.table.views
  (:require [mine-sweep.ui.record.table.styles :as styles]))

(defn- localized-time
  [time-str]
  (-> time-str
      (str "+00:00")
      js/Date.
      .toString
      (clojure.string/split "GMT")
      first
      clojure.string/trim))

(defn level-rows
  [level level-records hide-username?]
  (let [row-num (count level-records)]
    (reduce-kv (fn [acc idx {:keys [row_number record inserted_at username]}]
                 (conj acc
                       [:tr {:key (str level idx) :style (styles/level-text level)}
                        (when (zero? idx)
                          [:td {:style styles/record-table-col-level
                                :row-span row-num}
                           level])
                        [:td {:style styles/record-table-col-finish-time}
                         (-> record (/ 10) (str "s"))]
                        [:td {:style styles/record-table-col-datetime}
                         (localized-time inserted_at)]
                        (when-not hide-username?
                          [:td {:style styles/record-table-col-username}
                           username])]))
               []
               level-records)))

(defn compare-level
  [l1 l2]
  (case [l1 l2]
    ([:easy :medium] [:easy :hard] [:medium :hard])
    true

    false))

(defn table
  [{:keys [records-data hide-username?]}]
  (let [records-sorted-by-level (into (sorted-map-by compare-level) records-data)
        rows (reduce (fn [rows [level level-records]]
                          (into rows (level-rows level level-records hide-username?)))
                        []
                        records-sorted-by-level)]
    [:table {:style styles/record-table}
     (into
      [:tbody
       [:tr {:style styles/record-table-header}
        [:th {:style styles/record-table-col-level} "level"]
        [:th {:style styles/record-table-col-finish-time} "finish time"]
        [:th {:style styles/record-table-col-datetime} "date"]
        (when-not hide-username? [:th {:style styles/record-table-col-username} "username"])]]
      rows)]))
