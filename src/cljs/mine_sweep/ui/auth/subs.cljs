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

(rf/reg-sub
 ::error-msg-text
 (fn [db _]
   (:ui.auth/error-msg db)))

(rf/reg-sub
 :ui.auth/error-msg
 :<- [::error-msg-text]
 :<- [::form-submission-status]
 (fn [[error-msg status]]
   (when-not (= :submitting status)
     error-msg)))
