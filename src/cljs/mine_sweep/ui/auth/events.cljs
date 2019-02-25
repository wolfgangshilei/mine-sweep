(ns mine-sweep.ui.auth.events
  (:require [re-frame.core :as rf]
            [mine-sweep.utils.re-frame :refer [register-event-fx
                                               register-event-db]]
            [mine-sweep.api.auth :as auth-api]
            [mine-sweep.api.session :as session-api]
            [mine-sweep.config :as config]))

(rf/reg-fx
 ::server-login
 (fn [login-form]
   (auth-api/login
    login-form
    #(rf/dispatch [::handle-login-result %])
    #(rf/dispatch [::handle-auth-error %]))))

(rf/reg-fx
 ::server-signup
 (fn [signup-form]
   (auth-api/signup
    signup-form
    #(rf/dispatch [::handle-signup-result %])
    #(rf/dispatch [::handle-auth-error %]))))

(defn update-login-form
  [db [_ {:keys [username password]}]]
  (cond-> db
    username
    (assoc-in [:ui.auth/login-form :username] username)

    password
    (assoc-in [:ui.auth/login-form :password] password)))

(register-event-db
 :ui.auth/update-login-form
 update-login-form)

(defn update-signup-form
  [db [_ {:keys [username password]}]]
  (cond-> db
    username
    (assoc-in [:ui.auth/signup-form :username] username)

    password
    (assoc-in [:ui.auth/signup-form :password] password)))

(register-event-db
 :ui.auth/update-signup-form
 update-signup-form)

(defn submit-login-form
  [{:keys [db]} _]
  (let [login-form (get db :ui.auth/login-form)]
    {:db (-> db
             (assoc :ui.auth/form-submission-status :submitting))
     ::server-login login-form}))

(register-event-fx
 :ui.auth/submit-login-form
 submit-login-form)

(defn submit-signup-form
  [{:keys [db]} _]
  (let [signup-form (get db :ui.auth/signup-form)]
    {:db (-> db
             (assoc :ui.auth/form-submission-status :submitting))
     ::server-signup signup-form}))

(register-event-fx
 :ui.auth/submit-signup-form
 submit-signup-form)

(defn toggle-panel
  [db [_ panel]]
  (assoc db
         :ui.auth/panel panel
         :ui.auth/form-submission-status nil
         :ui.auth/errors nil
         :ui.auth/login-form nil
         :ui.auth/signup-form nil))

(register-event-db
 :ui.auth/toggle-panel
 toggle-panel)

(defn- clear-session
  ([db]
   (clear-session db nil))
  ([db _]
   (assoc db :ui.auth/session nil)))

(register-event-db
 :ui.auth/clear-session
 clear-session)

(defn- set-form-error
  [db reason]
  (-> db (assoc :ui.auth/form-submission-status :error
                :ui.auth/errors reason)))

(defn- handle-auth-result-fx
  [{db :db} [_ {:keys [result reason] :as res}]]
  (if (= result "ok")
    {:db                   (assoc db :ui.auth/form-submission-status nil)
     :dispatch-n           [[:ui.auth/toggle-panel :none]
                            [:ui.record/create-record]]
     :ui.auth/init-session nil}
    {:db (-> db
             (set-form-error reason)
             (clear-session))}))

(register-event-fx
 ::handle-login-result
 handle-auth-result-fx)

(register-event-db
 ::handle-auth-error
 (fn [db [_ {:keys [status]}]]
   (-> db
       (set-form-error {:http-status status})
       (clear-session))) )

(register-event-fx
 ::handle-signup-result
 handle-auth-result-fx)

(register-event-fx
 ::init-session
 (fn [{db :db} [_ {:keys [result data reason] :as res}]]
   (if (= result "ok")
     {:db (assoc db :ui.auth/session data)
      :dispatch [:ui.record/get-records (merge
                                         (select-keys data [:username])
                                         {:n        config/record-num
                                          :order-by :latest})]}
     {:db (clear-session db)})))

(defn init-session
  [_]
  (session-api/init-session
   #(rf/dispatch [::init-session %])
   #(rf/dispatch [:ui.auth/clear-session])))

(rf/reg-fx
 :ui.auth/init-session
 init-session)
