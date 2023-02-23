"""
Avatar dövüş oyunu
-> 4 element: Ateş, Su, Toprak, Hava

Her element birbirinden güçlü

-> Ateş > Toprak 
-> Toprak > Hava
-> Hava > Su
-> Su > Ateş


2 tane saldırı çeşidi var (damage - hasar) (* * *)
"""
"""
1. Kişilerim olacak bu kişileri ben kullanıcıdan alacağım
2. Kullanıcı kendi karakterini oluştururken ondan şu bilgileri alacağız:
  - İsim
  - Soyisim
  - Element
3. Bu bilgilerle bir kullanıcı oluşturacağız ve bunu bir listeye atacağız (sonraki aşamda dosyaya yazaılacak ve dosyadan okunacak)
4. Bot adı verilen bilgisayar tarafından otomatik oluşturulmuş kişiler üretilecek (bu botlarla, kişi dövüşecek)
5. Sıra tabalı strateji (saldırı seçilebilecek)
6. Kazanma ve kaybetme bilgileriniz tutulacak. Bunu da sözlükte tutabiliriz
"""
class Person():
  def __init__(self, name_surname, attacks, is_bot = False):
    self.name_surname = name_surname
    self.attacks = attacks
    self.is_bot = is_bot
    self.health = 20
    self.win = 0
    self.lose = 0
    self.element = self.__get_element()

  @classmethod
  def __get_element(cls):
    if cls is FireBender:
      return "Fire"
    if cls is WaterBender:
      return "Water"
    if cls is EarthBender:
      return "Earth"
    if cls is AirBender:
      return "Air"

  def won(self):
    self.win += 1

  def lost(self):
    self.lose += 1

  def get_rank(self):
    return self.win - self.lose

  def reset_health(self):
    self.health = 20

  def reset_current_turns(self):
    list(self.attacks.values())[1]["CurrentTurn"] = 0

class FireBender(Person):
  def __init__(self, name_surname, is_bot = False):
    self.overpowered_element = "Earth"
    self.attacks = {"Basic Fire Ball": {"Damage": 10, "Turn":0, "CurrentTurn":0, "Type":"Basic"}, "Dragon Breath": {"Damage":50, "Turn": 3, "CurrentTurn":0, "Type":"Special" }}
    super().__init__(name_surname, self.attacks, is_bot)

class WaterBender(Person):
  def __init__(self, name_surname, is_bot = False):
    self.overpowered_element = "Fire"
    self.attacks = {"Basic Water Ball": {"Damage": 10, "Turn":0, "CurrentTurn":0, "Type":"Basic"}, "Leviathan Breath": {"Damage":50, "Turn": 3, "CurrentTurn":0, "Type":"Special" }}
    super().__init__(name_surname, self.attacks, is_bot)

class EarthBender(Person):
  def __init__(self, name_surname, is_bot = False):
    self.overpowered_element = "Air"
    self.attacks = {"Basic Earth Ball": {"Damage": 10, "Turn":0, "CurrentTurn":0, "Type":"Basic"}, "Ancient Breath": {"Damage":50, "Turn": 3, "CurrentTurn":0, "Type":"Special" }}
    super().__init__(name_surname, self.attacks, is_bot)

class AirBender(Person):
  def __init__(self, name_surname, is_bot = False):
    self.overpowered_element = "Water"
    self.attacks = {"Basic Air Ball": {"Damage": 10, "Turn":0, "CurrentTurn":0, "Type":"Basic"}, "Giant Breath": {"Damage":50, "Turn": 3, "CurrentTurn":0, "Type":"Special" }}
    super().__init__(name_surname, self.attacks, is_bot)

"""
[
 {"Damage":50, "Turn": 3, "CurrentTurn":3, "Type":"Special" }
]

list(current_fighter.attacks.values())[1]["CurrentTurn"]
"""

fethi = EarthBender("Fethi Tekyaygil")
fethi.element

# !pip install faker
# Mock Data -- test datası 
# Bot 
from faker import Faker
fake = Faker()
from random import choice

elements = ["Fire","Air","Earth","Water"]

def CreateBender(choiced_element, name_surname):
  person = None

  if choiced_element == "Fire":
    person = FireBender(name_surname, True)
  if choiced_element == "Air":
    person = AirBender(name_surname, True)
  if choiced_element == "Earth":
    person = EarthBender(name_surname, True)
  if choiced_element == "Water":
    person = WaterBender(name_surname, True)

  return person

bot_enemy_list = []

for i in range(12):
  choiced_element = choice(elements)
   
  bot_enemy_list.append(CreateBender(choiced_element, fake.name()))

bot_enemy_list

user_dict = {}

elements = ["Fire", "Air", "Earth", "Water"]

name_surname = input("Please enter your name and surname: ")
if name_surname in user_dict:
  print("This name surname has been taken! Please try with different name surname!")
else:
  element_choice = int(input("Please select your element: \n1. Fire\n2. Air\n3. Earth\n4. Water\nIf you choose other options, your element will be chosen randomly! Your choice: "))
  user_element = None

  if element_choice not in [1,2,3,4]:
    user_element = choice(elements)
  else:
    user_element = elements[element_choice - 1]
  
  user_dict[name_surname] = CreateBender(user_element, name_surname)

  user_dict

  def select_fighter_and_opponent():
   fighter_string = ""

   for key, value in user_dict.items():
    fighter_string += f"- {key} ({value.element} Bender)\n"

   fighter_choice = input(f"Please select a fighter!\n{fighter_string}Your choice: ")
   fighter = user_dict[fighter_choice]

   enemy = choice(bot_enemy_list)

   return (fighter, enemy)

fighter, enemy = select_fighter_and_opponent()

enemy.name_surname

enemy.element

fighter.name_surname

fighter.element

enemy.overpowered_element

def turn(current_fighter):
  fighter_attack_damage = None

  fighter_attack_type_str = ""
  for key, value in current_fighter.attacks.items():
    fighter_attack_type_str += f"- {key} ({value})\n"

  while True:
    fighter_attack_type = input(f"Please enter your attack type:\n {fighter_attack_type_str}\nYour choice:")

    chosen_attack = current_fighter.attacks[fighter_attack_type]
    fighter_attack_damage = chosen_attack["Damage"]

    if chosen_attack["Type"] == "Basic":
      if list(current_fighter.attacks.values())[1]["CurrentTurn"] != 0:
        list(current_fighter.attacks.values())[1]["CurrentTurn"] += 1
        if list(current_fighter.attacks.values())[1]["CurrentTurn"] == list(current_fighter.attacks.values())[1]["Turn"]:
          list(current_fighter.attacks.values())[1]["CurrentTurn"] = 0

    if chosen_attack["Type"] == "Special" and chosen_attack["CurrentTurn"] == 0:
      chosen_attack["CurrentTurn"] += 1

    elif chosen_attack["Type"] == "Special" and chosen_attack["CurrentTurn"] != 0:
      print(f"Please enter a valid attack! You must wait {chosen_attack['Turn'] - chosen_attack['CurrentTurn']} turn to perform this attack!")
      continue

    return fighter_attack_damage

def fight(fighter, enemy):
  fighters = [fighter, enemy]
  idx = 0
  while True:
    current_fighter = fighters[idx % 2]
    enemy = fighters[(idx+1) % 2]

    print(f"Idx: {idx + 1}")
    print(f"{current_fighter.name_surname}: {current_fighter.health}")
    print(f"{enemy.name_surname}: {enemy.health}")
    
    attack_damage = turn(current_fighter)

    if current_fighter.overpowered_element == enemy.element:
      attack_damage *= 1.20

    enemy.health -= attack_damage

    if enemy.health <= 0:
      current_fighter.won()
      enemy.lost()

      current_fighter.reset_health()
      enemy.reset_health()
      
      current_fighter.reset_current_turns()
      enemy.reset_current_turns()
      

      print(f"Game is over! {current_fighter.name_surname} won!")

      break

    idx += 1

    fight(fighter, enemy)
    fighter.win
    enemy.lose



