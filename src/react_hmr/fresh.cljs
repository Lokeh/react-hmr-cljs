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
  (let [call (atom 0)
        saved-type (atom nil)
        custom-hooks? (atom nil)]
    (fn set-signature!
      ([& args]
       (js/console.log args)
       (case @call
         0 (let [[type key force-reset get-custom-hooks] args]
             (reset! saved-type type)
             (reset! custom-hooks? (fn? get-custom-hooks))
             (react-fresh/setSignature type key force-reset get-custom-hooks))
         1 (when @custom-hooks?
             (react-fresh/collectCustomHooksForSignature @saved-type))
         nil)
       (swap! call inc)))))
