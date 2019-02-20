(ns mine-sweep.ui.record.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [stylefy.core :as stylefy]
            [mine-sweep.ui.record.subs]
            [mine-sweep.ui.record.events]
            [mine-sweep.ui.record.styles :as styles]
            [mine-sweep.config :as config]))

(defn auth-buttons
  [style]
  [:div {:style (merge styles/record-panel
                       styles/flex-layout
                       style)}
   [:p {:style styles/auth-text}
    [:a {:style styles/auth-link
         :href "#"
         :on-click #(rf/dispatch [:ui.auth/toggle-panel :login])} "login"]
    " to keep your records."]])

(defn level-rows
  [level level-records]
  (let [row-num (count level-records)]
    (reduce-kv (fn [acc idx {:keys [row_number record inserted_at]}]
                 (conj acc
                       [:tr
                        (when (zero? idx)
                          [:td {:style styles/record-table-col-level
                                :row-span row-num}
                           level])
                        [:td {:style styles/record-table-col-finish-time}
                         (-> record (/ 10) (str "s"))]
                        [:td {:style styles/record-table-col-datetime}
                         (clojure.string/replace-first inserted_at "T" " ")]]))
               []
               level-records)))

(comment (level-rows :easy [{:record 14 :inserted_at "2019-02-11T09:05:13" :row_number 1}
                            {:record 14 :inserted_at "2019-02-11T09:05:13" :row_number 2}]))

(defn record-board
  [username records]
  (let [records-order (r/atom :latest)]
    (fn [username records]
      (let [records-data  (get records @records-order)
            rows          (reduce-kv (fn [rows level level-records]
                                       (into rows (level-rows level level-records)))
                                     []
                                     records-data)]
        [:<>
         [:div {:style styles/records-order-options}
          [:p {:style {:margin 0}} "records:"]
          [:label [:input {:type :radio
                           :name :records-type
                           :value :latest
                           :on-change #(do (reset! records-order :latest)
                                           (rf/dispatch [:ui.record/get-records {:username username
                                                                                 :n        config/record-num
                                                                                 :order-by :latest}]))
                           :checked (= @records-order :latest)}]
           (str "latest-" config/record-num)]
          [:label [:input {:type :radio
                           :name :records-type
                           :value :best
                           :on-change #(do (reset! records-order :best)
                                           (rf/dispatch [:ui.record/get-records {:username username
                                                                                 :n        config/record-num
                                                                                 :order-by :best}]))
                           :checked (= @records-order :best)}]
           (str "best-" config/record-num)]]
         [:table {:style styles/record-table}
          (into
           [:tbody
            [:tr {:style styles/record-table-header}
             [:th {:style styles/record-table-col-level} "level"]
             [:th {:style styles/record-table-col-finish-time} "finish time"]
             [:th {:style styles/record-table-col-datetime} "date"]]]
           rows)]]))))

(comment (record-board {:latest {:easy [{:record 14 :inserted_at "2019-02-11T09:05:13"}
                                        {:record 14 :inserted_at "2019-02-11T09:05:13"}]}}))

(defn user-info-board
  [{:keys [records username style]}]
  [:div {:style (merge styles/record-panel
                       styles/normal-layout
                       style)}
   [:p {:style styles/username-text} (str "Hello, ")
    [:span {:style styles/username} username] " !"]
   [record-board username records]])

(defn record-panel
  [{:keys [style] :as params}]
  (let [username (rf/subscribe [:ui.record/my-username])]
    (if @username
      [user-info-board (into params {:username @username
                                     :records  @(rf/subscribe [:ui.record/records @username])})]
      [auth-buttons style])))
