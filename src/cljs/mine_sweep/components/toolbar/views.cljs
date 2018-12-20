(ns mine-sweep.components.toolbar.views
  (:require [mine-sweep.components.toolbar.styles :as styles]))

(defn toolbar-item
  ([items]
   [toolbar-item {} items])
  ([{:keys [style title]} items]
   (into [:div {:style styles/toolbar-item-container}
          [:button {:style styles/toolbar-title} "levels"]]
         items)))

(defn select-level
  [current-level]
  [[:button {:style (merge styles/toolbar-item {:display "none"})} "easy" [:text {:style {:float "right"}} "*"]]
   [:button {:style (merge styles/toolbar-item {:display "none"})} "meduim"
    [:text {:style {:float "right"}} "*"]]
   [:button {:style (merge styles/toolbar-item {:display "none"})} "hard"]])
