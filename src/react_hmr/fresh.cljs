(ns react-hmr.fresh
  "An example implementation of a macro that registers and updates React
  components with the new hot-loading functionality, \"React Fresh\"."
  (:require [cljs-bean.core :refer [bean]]
            ["react-refresh/runtime" :as react-fresh])
  (:require-macros [react-hmr.fresh]))

(def ->props bean)

(defn register!
  "Registers a component with the React Fresh runtime.
  `type` is the component function, and `id` is the unique ID assigned to it
  (e.g. component name) for cache invalidation."
  [type id]
  (js/console.log type id)
  (react-fresh/register type id))

(defn signature! []
  (let [saved-type (atom nil)
        custom-hooks? (atom nil)]
    (fn set-signature!
      ;; this gets called within the component to enable reload of custom hooks
      ;; outside the file the component is defined in
      ([]
       (js/console.log @saved-type @custom-hooks?)
       (when @custom-hooks?
         (react-fresh/collectCustomHooksForSignature @saved-type)))
      ;; the rest get called at the top-level
      ([type key]
       (set-signature! type key nil nil))
      ([type key force-reset]
       (set-signature! type key force-reset nil))
      ([type key force-reset get-custom-hooks]
       (js/console.log type key force-reset get-custom-hooks)
       (reset! saved-type type)
       (reset! custom-hooks? (fn? get-custom-hooks))
       (react-fresh/setSignature type key force-reset get-custom-hooks)))))
