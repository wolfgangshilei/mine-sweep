(ns mine-sweep.events
  (:require #_[re-frame.core :as rf]
            [mine-sweep.db :refer [default-db]]
            [mine-sweep.utils.re-frame :refer [register-event-fx]]
            [mine-sweep.ui.game-panel.mine-field.events]
            [mine-sweep.ui.game-panel.events]
            [mine-sweep.ui.auth.events]
            #_[mine-sweep.ui.common.constants :as const]))

(register-event-fx
 ::initialize-db
 (fn [{:keys [db]}]
   {:db                   default-db
    :ui.auth/init-session nil
    :dispatch             [:ui.game/restart-game]}))
