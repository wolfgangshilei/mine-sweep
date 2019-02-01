(ns mine-sweep.ui.auth.db
  [:require [cljs.spec.alpha :as spec]])

(spec/def ::username string?)
(spec/def ::password string?)

(spec/def :ui.auth/login-form (spec/nilable (spec/keys :opt-un [::username ::password])))

(spec/def :ui.auth/form-submission-status (spec/nilable #{:submitting :error}))
(spec/def :ui.auth/error-msg (spec/nilable string?))

(spec/def :ui.auth/panel #{:none :login :signup})

(spec/def :ui.auth/session (spec/nilable (spec/keys :req-un [::username])))
