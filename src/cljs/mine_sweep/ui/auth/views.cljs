(ns mine-sweep.ui.auth.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [mine-sweep.ui.auth.styles :as styles]
            [mine-sweep.ui.auth.subs]
            [stylefy.core :as stylefy]))

(defn form-input
  [{:keys [label type on-blur disabled]}]
  (let [!input (r/atom nil)]
    (r/create-class
     {:component-will-update
      (fn [this new-argv]
        (let [prev-panel-display (-> (r/props this) :panel-display)
              new-panel-display  (-> (fnext new-argv) :panel-display)]
          (when (not= prev-panel-display new-panel-display)
             (set! (.-value @!input) ""))))

      :reagent-render
      (fn [{:keys [label type on-blur disabled]}]
        [:div {:style styles/input-wrapper}
         [:label label]
         [:input (stylefy/use-style styles/form-input-style
                                    {:type     type
                                     :ref      (fn [el] (reset! !input el))
                                     :disabled disabled
                                     :on-blur  on-blur})]])})))

(def auth-panel-data
  {:login  {:title-text       "Login"
            :toggle-link-text "Signup"
            :toggle-to        :signup
            :update-event     :ui.auth/update-login-form
            :submit-text      "login"
            :submit-event     :ui.auth/submit-login-form}
   :signup {:title-text       "Signup"
            :toggle-link-text "Login"
            :toggle-to        :login
            :update-event     :ui.auth/update-signup-form
            :submit-text      "signup"
            :submit-event     :ui.auth/submit-signup-form}})

(defn auth-panel
  [panel]
  (let [disable?          (rf/subscribe [:ui.auth/disable-auth-panel?])
        error-msg         (rf/subscribe [:ui.auth/error-msg])
        panel-data        (get auth-panel-data panel)]
    [:div (stylefy/use-style (styles/auth-panel @disable?))
     [:div {:style styles/panel-header-wrapper}
      [:p {:style styles/title} (:title-text panel-data)]
      [:a {:style styles/toggle-link
           :href "#"
           :on-click #(when-not @disable? (rf/dispatch [:ui.auth/toggle-panel (:toggle-to panel-data)]))}
       (:toggle-link-text panel-data)]]
     [:div {:style styles/error-message} @error-msg]
     (conj
      [:form {:style styles/form-body}]
      [form-input {:label         "Username:"
                   :type          :text
                   :required      true
                   :disabled      @disable?
                   :panel-display panel
                   :on-blur       (fn [e]
                                    (rf/dispatch [(:update-event panel-data)
                                                  {:username
                                                   (-> e .-target .-value)}]))}]
      [form-input {:label         "Password:"
                   :type          :password
                   :disabled      @disable?
                   :panel-display panel
                   :on-blur       (fn [e]
                                    (rf/dispatch [(:update-event panel-data)
                                                  {:password
                                                   (-> e .-target .-value)}]))}]
      [:div {:style styles/form-btns-wrapper}
       [:button (stylefy/use-style styles/submit-btn
                                   {:type     :button
                                    :disabled @disable?
                                    :on-click #(rf/dispatch [(:submit-event panel-data)])})
        (:submit-text panel-data)]
       [:button (stylefy/use-style styles/cancel-btn
                                   {:type     :button
                                    :disabled @disable?
                                    :on-click #(rf/dispatch [:ui.auth/toggle-panel :none])})
        "Cancel"]])]))
