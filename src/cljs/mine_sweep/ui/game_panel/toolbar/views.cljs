(ns mine-sweep.ui.game-panel.toolbar.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [mine-sweep.ui.game-panel.toolbar.styles :as styles]))

(defn- dropdown-menu-item
  [{:keys [text selected? show? on-select]}]
  [:button {:style    (styles/dropdown-menu-item show?)
            :on-click #(on-select text)}
   [:p {:style styles/dropdown-menu-text}
    (when selected?
      [:span {:style styles/dropdown-menu-item-selected-mark} "*"])
    text]])

(defn- dropdown-menu
  ([{:keys [title items default-selection on-item-selected]}]
   (let [expanded? (r/atom false)]
     (fn [{:keys [default-selection]}]
       (into [:div {:style          styles/dropdown-menu
                    :on-click       #(reset! expanded? true)
                    :on-mouse-leave #(reset! expanded? false)}
              [:button {:style (merge (styles/dropdown-menu-title @expanded?)
                                      styles/dropdown-menu-text)} title]]
             (map #(dropdown-menu-item {:text      %
                                        :selected? (= default-selection %)
                                        :show?     @expanded?
                                        :on-select (fn [item] (reset! expanded? false)
                                                     (on-item-selected item))})
                  items))))))

(defn toolbar []
  (let [selected-level (rf/subscribe [:ui.game/current-level])]
    [:div {:style styles/toolbar}
     [dropdown-menu
      {:title             "levels"
       :items             ["easy" "medium" "hard"]
       :on-item-selected  #(rf/dispatch [:ui.game/set-level (keyword %)])
       :default-selection (name @selected-level)}]]))
