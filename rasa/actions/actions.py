from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.events import SlotSet
from rasa_sdk.executor import CollectingDispatcher
import requests

class ActionJoinGroup(Action):

    def name(self) -> Text:
        return "action_join_group_with_preferences"

    #simulacion
    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        
        group_name = tracker.get_slot("group_name")
        user_email = tracker.get_slot("email")
        preference = tracker.get_slot("preference")
        
        availability_start = ["2024-12-01"]
        availability_end = ["2024-12-10"]
        
        payload = {
            "groupName": group_name,
            "email": user_email,
            "preference": preference,
            "availabilityStartDates": availability_start,
            "availabilityEndDates": availability_end
        }
        
        response = requests.post("http://backend:8081/api/groups/joinWithPreferences", json=payload)
        
        if response.status_code == 200:
            dispatcher.utter_message(text="Te has unido al grupo exitosamente.")
        else:
            dispatcher.utter_message(text="No se pudo unir al grupo. Inténtalo de nuevo.")
        
        return []

class ActionGetAvailableGroups(Action):

    def name(self) -> Text:
        return "action_get_available_groups"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        try:
            response = requests.get("http://backend:8081/api/groups/public")
            if response.status_code == 200:
                groups = response.json()  
                if groups:
                    group_list = ", ".join([group["name"] for group in groups])
                    dispatcher.utter_message(text=f"Estos son los grupos públicos disponibles: {group_list}")
                else:
                    dispatcher.utter_message(text="No hay grupos públicos disponibles en este momento.")
            else:
                dispatcher.utter_message(text="No se pudo obtener la lista de grupos. Por favor, inténtalo más tarde.")
        except Exception as e:
            dispatcher.utter_message(text="Ocurrió un error al obtener los grupos disponibles.")
            print(f"Error: {e}")
        return []

class ActionClosePrivateGroup(Action):

    def name(self) -> Text:
        return "action_close_private_group"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        
        group_name = tracker.get_slot("group_name")
        response = requests.post(f"http://backend:8081/api/groups/closePrivate/{group_name}")
        
        if response.status_code == 200:
            dispatcher.utter_message(text="El grupo se cerró y las fechas fueron recomendadas.")
        else:
            dispatcher.utter_message(text="No se pudo cerrar el grupo o recomendar fechas.")
        
        return []

class ActionGetGroupsByPreference(Action):

    def name(self) -> Text:
        return "action_get_groups_by_preference"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        
        preference = tracker.get_slot("preference")
        
        if not preference:
            dispatcher.utter_message(text="No entendí tu preferencia. Por favor, indícala de nuevo.")
            return []

        try:
            response = requests.get(f"http://backend:8081/api/groups/triptype/{preference}")
            if response.status_code == 200:
                groups = response.json()
                if groups:
                    group_list = ", ".join([group["name"] for group in groups])
                    dispatcher.utter_message(
                        text=f"Estos son los grupos disponibles con temática '{preference}': {group_list}"
                    )
                else:
                    dispatcher.utter_message(
                        text=f"No se encontraron grupos disponibles con temática '{preference}'."
                    )
            else:
                dispatcher.utter_message(
                    text="Hubo un problema al obtener los grupos disponibles. Inténtalo más tarde."
                )
        except Exception as e:
            dispatcher.utter_message(text="Ocurrió un error al comunicarse con el servidor.")
            print(f"Error: {e}")

        return []
    
class ActionGetGroupsByAudience(Action):
    def name(self) -> Text:
        return "action_get_groups_by_audience"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        intent = tracker.latest_message['intent']['name']
        audience = intent.replace('audience_', '').capitalize()

        response = requests.get(f"http://backend:8081/api/groups/audience/{audience}")

        if response.status_code == 200:
            groups = response.json()
            if groups:
                group_list = ", ".join(group["name"] for group in groups)
                dispatcher.utter_message(text=f"Estos son los grupos disponibles para {audience}: {group_list}")
            else:
                dispatcher.utter_message(text=f"No hay grupos disponibles para {audience}.")
        else:
            dispatcher.utter_message(text="No se pudo obtener la información de grupos en este momento.")

        return []

class ActionSetPreferenceCultural(Action):
    def name(self) -> Text:
        return "action_set_preference_cultural"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Cultural")]


class ActionSetPreferencePlaya(Action):
    def name(self) -> Text:
        return "action_set_preference_playa"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Playa")]


class ActionSetPreferenceRomantica(Action):
    def name(self) -> Text:
        return "action_set_preference_romantica"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Romántica")]


class ActionSetPreferenceRelax(Action):
    def name(self) -> Text:
        return "action_set_preference_relax"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Relax")]


class ActionSetPreferenceAventura(Action):
    def name(self) -> Text:
        return "action_set_preference_aventura"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Aventura")]


class ActionSetPreferenceGastronomica(Action):
    def name(self) -> Text:
        return "action_set_preference_gastronomica"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Gastronómica")]


class ActionSetPreferenceBienestar(Action):
    def name(self) -> Text:
        return "action_set_preference_bienestar"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Bienestar")]


class ActionSetPreferenceMontana(Action):
    def name(self) -> Text:
        return "action_set_preference_montana"

    def run(self, dispatcher, tracker, domain):
        return [SlotSet("preference", "Montaña")]

