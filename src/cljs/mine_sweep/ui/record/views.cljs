(ns mine-sweep.ui.record.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [stylefy.core :as stylefy]
            [mine-sweep.ui.record.subs]
            [mine-sweep.ui.record.events]
            [mine-sweep.ui.record.styles :as styles]
            [mine-sweep.config :as config]
            [mine-sweep.ui.record.tabs.views :as tabs-view]
            [mine-sweep.ui.record.table.views :as table]
            [mine-sweep.js-modules.react-transition-group :refer [css-transition]]))

(defn auth-buttons
  [style]
  [:div {:style styles/flex-layout}
   [:div {:style styles/login-text}
    [css-transition styles/login-link-transition
     [:div (stylefy/use-style styles/login-link
                              {
                               :on-click #(rf/dispatch [:ui.auth/toggle-panel :login])})
      "login"]]
    " to keep your records."]])


(defn personal-record-board
  [username records]
  (let [records-order (r/atom :latest)]
    (fn [username records]
      (let [records-data   (get records @records-order)
            rows-per-level (:user config/records-per-level)]
        [:<>
         [:div {:style styles/records-order-options}
          [:p {:style {:margin 0}} "records:"]
          [:label [:input {:type :radio
                           :name :records-type
                           :value :latest
                           :on-change #(do (reset! records-order :latest)
                                           (rf/dispatch [:ui.record/get-records {:username username
                                                                                 :n        rows-per-level
                                                                                 :order-by :latest}]))
                           :checked (= @records-order :latest)}]
           (str "latest-" rows-per-level)]
          [:label [:input {:type :radio
                           :name :records-type
                           :value :best
                           :on-change #(do (reset! records-order :best)
                                           (rf/dispatch [:ui.record/get-records {:username username
                                                                                 :n        rows-per-level
                                                                                 :order-by :best}]))
                           :checked (= @records-order :best)}]
           (str "best-" rows-per-level)]]
         [table/table {:records-data   records-data
                       :hide-username? true}]]))))

(comment (personal-record-board {:latest {:easy [{:record 14 :inserted_at "2019-02-11T09:05:13"}
                                                 {:record 14 :inserted_at "2019-02-11T09:05:13"}]}}))

(defn all-time-best
  [{:keys [records]}]
  [:div {:style styles/normal-layout}
   [table/table {:records-data   (:best records)
                 :hide-username? false}]])

(defn personal-records
  [{:keys [records username]}]
  [:div {:style styles/normal-layout}
   [personal-record-board username records]])

(defn record-panel
  [{:keys [style] :as params}]
  (let [username (rf/subscribe [:ui.record/my-username])]
    [:div (stylefy/use-style (merge style
                                    styles/record-panel))
     [tabs-view/tabs {:tabs [{:label "all time best"
                              :on-select #(rf/dispatch [:ui.record/get-all-time-best-records
                                                        {:username :all-time-best
                                                         :n        (:all-time-best config/records-per-level)
                                                         :order-by :best}])}
                             {:label (str (if @username
                                            (str @username "'s")
                                            "my")
                                          " records")
                              :on-select #(when @username
                                            (rf/dispatch [:ui.record/get-records {:username @username
                                                                                  :n        (:user config/records-per-level)
                                                                                  :order-by :latest}]))}]}
      [all-time-best {:records @(rf/subscribe [:ui.record/records :all-time-best])}]
      (if @username
        [personal-records {:username @username
                           :records  @(rf/subscribe [:ui.record/records @username])}]
        [auth-buttons])]]))
