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

    'Fat' : 'Fat',
    'fat' : 'fat',
    'Carb.' : 'Carbohydrate',
    'Carbohydrate' : 'Carbohydrate',
    'Sugars' : 'Sugars',
    'Protein' : 'Protein',
    'Sodium' : 'Sodium',
    #'Saturated Fat' : 'SatFat',
    #'Trans Fat' : 'TransFat',
    'Cholestrerol' : 'Cholestrerol',
    #'Dietary Fiber' : 'DietaryFiber',
    'Fiber' : 'Fiber',
    'Calories' : 'Calories'
}

unit_list = ['mg', 'g', '%']
