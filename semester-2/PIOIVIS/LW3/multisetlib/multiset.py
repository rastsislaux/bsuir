"""
THIS FILE IS A PART OF MULTISETLIB (LABORATORY WORK #3)
AUTHOR: ROSTISLAV LIPSKY
DATE:   11.02.2022
"""

class Multiset(list):
    """
    This class implements multiset in Python
    """

    def __init__(self, lst: list = None):
        if lst is None:
            super().__init__()
            return

        super().__init__(lst)

    def union(self, other):
        """
        Find union of multisets
        :param other: multiset
        :return: multiset (union)
        """
        result = self.copy()
        for element in other:
            result += [element for i in range(other.count(element) - result.count(element))]
        return result

    def copy(self):
        return self.__copy__()

    def __copy__(self):
        return Multiset(super().copy())

    def __repr__(self):
        return f"<Multiset: {super().__repr__()}>"

    def __str__(self):
        return f"<Multiset: {super().__repr__()}>"

    def __eq__(self, other):
        if type(other) == type(self):
            if len(other) != len(self):
                return False
            for element in self:
                if self.count(element) != other.count(element):
                    return False
            return True
        else:
            return False

