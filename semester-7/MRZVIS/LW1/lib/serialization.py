import abc
import json
import lzma
import pickle

import numpy as np


class SerializationStrategy(abc.ABC):
    def save(self, path, ae, compressed_data, w, h, input_dim, output_dim, m, n):
        rsx_file = {
            "metadata": {
                "original_width": w,
                "original_height": h,
                "input_dim": input_dim,
                "output_dim": output_dim,
                "m": m,
                "n": n,
            },
            "weights": ae.W2,
            "data": np.around(compressed_data, decimals=2),
        }

        self.do_save(rsx_file, path)

    def do_save(self, file, path):
        pass

    def load(self, file_path):
        rsx = self.load_rsx(file_path)
        return (
            rsx['metadata']['original_width'],
            rsx['metadata']['original_height'],
            rsx['metadata']['input_dim'],
            rsx['metadata']['output_dim'],
            rsx['metadata']['m'],
            rsx['metadata']['n'],
            rsx['weights'],
            rsx['data']
        )

    def load_rsx(self, file_path) -> dict:
        pass

class JsonSerializationStrategy(SerializationStrategy):
    def do_save(self, file, path):
        file['weights'] = file['weights'].tolist()
        file['data'] = file['data'].tolist()

        with open(path, "w") as f:
            json.dump(file, f)

        print(f"All data saved to {path} as JSON.")

    def load_rsx(self, path):
        with open(path, 'r') as f:
            loaded_data = json.load(f)

        loaded_data['weights'] = np.array(loaded_data['weights'])
        loaded_data['data'] = np.array(loaded_data['data'])

        return loaded_data


class PickleSerializationStrategy(SerializationStrategy):
    def do_save(self, file, path):
        file['weights'] = file['weights']
        file['data'] = file['data']

        with open(path, "wb") as f:
            pickle.dump(file, f)

        print(f"All data saved to {path} as pickle.")

    def load_rsx(self, path):
        with open(path, 'rb') as f:
            loaded_data = pickle.load(f)

        loaded_data['weights'] = loaded_data['weights']
        loaded_data['data'] = loaded_data['data']

        return loaded_data


class LzmaSerializationStrategy(SerializationStrategy):
    def do_save(self, file, path):
        file['weights'] = file['weights']
        file['data'] = file['data']

        with lzma.open(path, "wb") as f:
            pickle.dump(file, f)

        print(f"All data saved to {path} as lzma-pickle.")

    def load_rsx(self, path):
        with lzma.open(path, 'rb') as f:
            loaded_data = pickle.load(f)

        loaded_data['weights'] = loaded_data['weights']
        loaded_data['data'] = loaded_data['data']

        return loaded_data
