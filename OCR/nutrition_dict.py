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
nutri_result_dict = {
    'SatFat' : 'SaturatedFat',
    'TransFat' : 'TransFat',
    'Fiber' : 'DietaryFiber',
    'Calories': 'Calories',
    'Carbohydrate' : 'Carbohydrate',
    'Protein' : 'Protein',
    'Fat' : 'Fat',
    'Sugars' : 'Sugars',
    'Sodium' : 'Sodium',
    'Calcium' : 'Calcium',
    'Cholesterol' : 'Cholesterol'
}

nutri_dict = {
    #"Calories from Fat" : 'CalroriesFromFat',
    #'Calories' : 'Calories',

    'SaturatedFat' : nutri_result_dict['SatFat'],
    'Saturated Fat' : nutri_result_dict['SatFat'],
    'Trans Fat' : nutri_result_dict['TransFat'],
    'TransFat' : nutri_result_dict['TransFat'],
    'DietaryFiber' : nutri_result_dict['Fiber'],
    'Dietary Fiber' : nutri_result_dict['Fiber'],
    '식이섬유':nutri_result_dict['Fiber'],

    '열량' : nutri_result_dict['Calories'],
    '탄수화물' : nutri_result_dict['Carbohydrate'],
    '단백질': nutri_result_dict['Protein'],
    '지방': nutri_result_dict['Fat'],
    '포화지방': nutri_result_dict['SatFat'],
    '트랜스지방': nutri_result_dict['TransFat'],
    '당류' : nutri_result_dict['Sugars'],
    '콜레스테롤': nutri_result_dict['Cholesterol'],
    '나트륨': nutri_result_dict['Sodium'],
    '칼슘':nutri_result_dict['Calcium'],
    'Fat' : nutri_result_dict['Fat'],
    'fat' : nutri_result_dict['Fat'],
    'Carb.' : nutri_result_dict['Carbohydrate'],
    'Carbohydrate' : nutri_result_dict['Carbohydrate'],
    'Sugars' : nutri_result_dict['Sugars'],
    'Protein' : nutri_result_dict['Protein'],
    'Sodium' : nutri_result_dict['Sodium'],
    'Cholesterol' : nutri_result_dict['Cholesterol'],
    'Fiber' : nutri_result_dict['Fiber'],
    'Calories' : nutri_result_dict['Calories'],
    'Calcium': nutri_result_dict['Calcium']
}

unit_list = ['mg', 'g', '%']
