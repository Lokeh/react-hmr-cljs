(ns react-hmr.dep-z
  (:require ["react" :as react]
            [react-hmr.react :refer [$]]
            [react-hmr.refresh :as refresh :refer [defnc]]
            [react-hmr.dep-b :as b]))

(defnc component []
  (let [[count set-count] (react/useState 0)]
    ($ :div {:style {:background "#eee"}}
       ($ :div ($ :button {:onClick #(set-count inc)} "+ " count))
       (b/greet "child in another file"))))
