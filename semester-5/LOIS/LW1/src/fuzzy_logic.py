################################################################################################
#                                                                                              #
#   Задача: реализовать прямой нечеткий логический вывод                                       #
#   Вариант: 21. Реализовать прямой нечеткий логический вывод, используя импликацию Гёделя     #
#   Авторы: Липский Р. В.   (121701)                                                           #
#           Жолнерчик И. А. (121701)                                                           #
#           Стронгин А. В.   (121701)                                                          #
#                                                                                              #
################################################################################################

class FuzzyLogic:

    @staticmethod
    def _impl(set1, set2):  # Godel implication
        result = []
        for a in set1:
            for b in set2:
                temp = b[1] if a[1] > b[1] else 1
                result.append((a[0], b[0], temp))
        return result

    @staticmethod
    def _and(set1, matrix):
        data = []
        for a in set1:
            for cell in matrix:
                if cell[0] == a[0]:
                    data.append((cell[0], cell[1], min(cell[2], a[1])))

        max_third_elements = {}

        for _, second, third in data:
            if second not in max_third_elements or third > max_third_elements[second]:
                max_third_elements[second] = third

        filtered_data = [(entry[1], entry[2])
                         for entry in data
                         if entry[1] in max_third_elements and entry[2] == max_third_elements[entry[1]]]

        return list(set(filtered_data))

    def _infer(self, set1, set2, set3):
        impl_result = self._impl(set2, set3)
        and_result = self._and(set1, impl_result)
        return and_result

    @staticmethod
    def _find_fitting_sets(sample_set, sets):

        def get_element_names(some_set):
            return set(map(lambda x: x[0], some_set))

        sample_names = get_element_names(sample_set)

        results = {}
        for name, a_set in sets.items():
            a_set_names = get_element_names(a_set)
            if a_set_names == sample_names:
                results[name] = a_set

        return results

    @staticmethod
    def _rule_to_str(rule):
        pairs = []
        for pair in rule:
            pairs.append(f"<{pair[0]}, {pair[1]}>")
        return "{" + ",".join(pairs) + "}"

    def run_inference(self, sets, rules, show_duplicates=False):
        old_size = 0
        new_size = 1

        results = []
        while old_size != new_size:
            old_size = len(sets)
            for rule in rules:
                set2 = sets[rule[0]]
                set3 = sets[rule[1]]
                sets1 = self._find_fitting_sets(set2, sets)
                for set1_name, set1 in sets1.items():
                    new_name = f"I{len(sets)}"
                    inference = self._infer(set1, set2, set3)

                    if set(inference) in map(lambda x: set(x), sets.values()):
                        if show_duplicates:
                            results.append(f"# {set1_name}/~\\({rule[0]}~>{rule[1]}) = {self._rule_to_str(inference)}")
                        continue

                    sets[new_name] = inference
                    results\
                        .append(f"{new_name} = {set1_name}/~\\({rule[0]}~>{rule[1]}) = {self._rule_to_str(inference)}")
            new_size = len(sets)
        return results
