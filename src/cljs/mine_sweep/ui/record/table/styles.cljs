(ns mine-sweep.ui.record.table.styles)


(def record-table
  {:border-collapse "collapse"
   :width           "100%"
   :table-layout    "fixed"
   :text-align      "center"})

(def record-table-header
  {:background-color "#CCC"
   :color            "black"
   :font-size        "1.1em"})

(def record-table-col-level
  {:width         "20%"
   :text-overflow "ellipsis"
   :overflow      "hidden"
   :border        "1px solid #ddd"})

(def record-table-col-username
  {:width         "25%"
   :text-overflow "ellipsis"
   :overflow      "hidden"
   :border        "1px solid #ddd"})

(def record-table-col-finish-time
  {:width         "15%"
   :text-overflow "ellipsis"
   :overflow      "hidden"
   :border        "1px solid #ddd"})

(def record-table-col-datetime
  {:text-overflow "ellipsis"
   :overflow      "hidden"
   :border        "1px solid #ddd"})

(defn level-text
  [level]
  {:color (condp = level
            :easy   "lightgreen"
            :medium "yellow"
            :hard   "deeppink")
   :text-weight "bold"})
