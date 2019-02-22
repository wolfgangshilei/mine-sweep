(ns mine-sweep.js-modules.react-transition-group)

(defmacro def-transition!
  [transition-name styles opts]
  `(let [a# (str "react-transition-group-" (hash ~styles))]
     (doall (map (fn [[suffix# style#]]
                   (when (#{:appear :appear-active
                            :enter :enter-active :enter-done
                            :exit :exit-active :exit-done}
                          suffix#)
                     (stylefy.core/class (str a# "-" (name suffix#)) style#))) ~styles))
     (def ~transition-name (assoc ~opts :class-names a#))))
