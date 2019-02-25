(ns mine-sweep.ui.auth.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::form-submission-status
 (fn [db _]
   (:ui.auth/form-submission-status db)))

(rf/reg-sub
 :ui.auth/disable-auth-panel?
 :<- [::form-submission-status]
 (fn [status]
   (= :submitting status)))

(defn- status->text
  [http-status]
  (case http-status
    nil nil
    (401 400) "Wrong username or password"
    "Server is now unavailable, please try it again later"))

(defn- flatten-text
  [error-list field]
  (when error-list
    (reduce #(str %1 field " " %2 "\n") "" error-list)))

(defn format-auth-form-errors
  [errors]
  (-> errors
      (update :http-status status->text)
      (update :username flatten-text "username")
      (update :password flatten-text "password")))

(rf/reg-sub
 ::errors
 (fn [db _]
   (when-let [errors (:ui.auth/errors db)]
     (format-auth-form-errors errors))))

(rf/reg-sub
 :ui.auth/errors
 :<- [::errors]
 :<- [::form-submission-status]
 (fn [[error-msg status]]
   (when-not (= :submitting status)
     error-msg)))
