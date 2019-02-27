(ns mine-sweep.ui.record.tabs.views
  (:require [reagent.core :as r]
            [stylefy.core :as stylefy]
            [mine-sweep.ui.record.tabs.styles :as styles]))

(defn tabs
  [{:keys [tabs]} & children]
  (when-not (= (count tabs)
               (count children))
    (throw (js/Error. (str ::tabs ": children number ("
                           (count children)
                           ") must be equal to the tab-labels' number ("
                           (count tabs)
                           ")"))))
  (let [selected-tab-idx (r/atom 0)]
    (fn [{:keys [tabs]} & children]
      (when-let [on-select-fn (-> tabs (nth @selected-tab-idx) :on-select)]
        (on-select-fn))
      [:div {:style {:display "flex"
                     :flex-direction "column"}}
       (into [:div {:style {:display "flex"
                            :text-align "center"}}]
             (map-indexed (fn [idx {label :label on-select :on-select}]
                            (vec [:div (stylefy/use-style (if (= idx @selected-tab-idx)
                                                            styles/tab-btn-selected
                                                            styles/tab-btn)
                                                          {:key idx
                                                           :on-click #(reset! selected-tab-idx idx)})
                                  label])) tabs))
       (nth children @selected-tab-idx)])))
