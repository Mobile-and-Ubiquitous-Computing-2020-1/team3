sanitize_dict = {
    'Nutrition Facts' : 0,
    'about' : 0,
    'About' : 0,
    'Total' : 0,
    '% Daily Value' : 0,
    'Daily Value' : 0,
}
value_dict = {
    'Incl.' : 1,
    'Include' : 1,
    'Serving Size': 2,
    'Serving Per Container' : 1, # check back and if there is no number, check front
}


nutri_dict = {
    #"Calories from Fat" : 'CalroriesFromFat',
    #'Calories' : 'Calories',

    '열량' : 'Calories',
    '탄수화물' : 'Carbohydrate',
    '단백질': 'Protein',
    '지방': 'Fat',
    '포화지방': 'SaturatedFat',
    '트랜스지방': 'TransFat',
    '식이섬유':'Fiber',
    '당류' : 'Sugars',
    '콜레스테롤': 'Cholesterol',
    '나트륨': 'Sodium',
    '칼슘':'Calcium',
    'Fat' : 'Fat',
    'fat' : 'Fat',
    'Carb.' : 'Carbohydrate',
    'Carbohydrate' : 'Carbohydrate',
    'Sugars' : 'Sugars',
    'Protein' : 'Protein',
    'Sodium' : 'Sodium',
    #'Saturated Fat' : 'SatFat',
    #'Trans Fat' : 'TransFat',
    'Cholesterol' : 'Cholesterol',
    #'Dietary Fiber' : 'DietaryFiber',
    'Fiber' : 'Fiber',
    'Calories' : 'Calories',
    'Calcium':'Calcium'
}

unit_list = ['mg', 'g', '%']
