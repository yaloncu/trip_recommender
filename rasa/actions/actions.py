from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher

class ActionRecommendGroup(Action):
    def name(self) -> Text:
        return "action_recommend_group"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        preference = tracker.get_slot('preference')

        # Aquí conectas con tu backend para obtener grupos
        recommended_groups = ["Grupo Aventura", "Grupo Familiar"]
        dispatcher.utter_message(text=f"Te recomiendo: {', '.join(recommended_groups)}")
        return []
