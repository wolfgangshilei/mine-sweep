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
   (when login-form
     (auth-api/login
      login-form
      #(rf/dispatch [::handle-login-result %])
      #(rf/dispatch [::handle-login-error %])))))

(rf/reg-fx
 ::server-signup
 (fn [signup-form]
   (when signup-form
     (auth-api/signup
      signup-form
      #(rf/dispatch [::handle-signup-result %])
      #(rf/dispatch [::handle-signup-error %])))))

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
    (if login-form
      {:db (-> db
               (assoc :ui.auth/form-submission-status :submitting))
       ::server-login login-form}
      {:db (-> db
               (assoc :ui.auth/form-submission-status :error
                      :ui.auth/error-msg              "Fill in username and password."))})))

(register-event-fx
 :ui.auth/submit-login-form
 submit-login-form)

(defn submit-signup-form
  [{:keys [db]} _]
  (let [signup-form (get db :ui.auth/signup-form)]
    (if signup-form
      {:db (-> db
               (assoc :ui.auth/form-submission-status :submitting))
       ::server-signup signup-form}
      {:db (-> db
               (assoc :ui.auth/form-submission-status :error
                      :ui.auth/error-msg              "Fill in username and password."))})))

(register-event-fx
 :ui.auth/submit-signup-form
 submit-signup-form)

(defn toggle-panel
  [db [_ panel]]
  (assoc db
         :ui.auth/panel panel
         :ui.auth/form-submission-status nil
         :ui.auth/error-msg nil
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
                :ui.auth/error-msg reason)))

(defn- handle-auth-result-fx
  [{db :db} [_ {:keys [result reason] :as res}]]
  (if (= result "ok")
    {:db (assoc db :ui.auth/form-submission-status nil)
     :dispatch [:ui.auth/toggle-panel :none]
     :ui.auth/init-session nil}
    {:db (-> db
             (set-form-error reason)
             (clear-session))}))

(register-event-fx
 ::handle-login-result
 handle-auth-result-fx)

(defn handle-login-error
  [db [_ {:keys [status]}]]
  (-> db
      (set-form-error "Invalid username or password.")
      (clear-session)))

(register-event-db
 ::handle-login-error
 handle-login-error)

(register-event-fx
 ::handle-signup-result
 handle-auth-result-fx)

(defn handle-signup-error
  [db [_ res]]
  (-> db
      (set-form-error "Invalid username or password.")
      (clear-session)))

(register-event-db
 ::handle-signup-error
 handle-signup-error)

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
